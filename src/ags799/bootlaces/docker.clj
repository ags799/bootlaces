(ns ags799.bootlaces.docker
  (:require [clojure.java.shell :refer [sh]]
            [clojure.java.io :as io]
            [boot.core :as boot]
            [boot.util :as util]))

(defn- default-dockerfile [jar-path]
  (let [entrypoint (format "ENTRYPOINT [\"java\",\"-jar\",\"%s\"]" jar-path)]
    (clojure.string/join "\n" ["FROM openjdk:8-jre"
                               (format "COPY %s ." jar-path)
                               entrypoint])))

(defn- spit-default-dockerfile [dockerfile jar-path]
  (spit dockerfile (default-dockerfile jar-path)))

(defn- find-jar [files]
  (boot/tmp-path (first (boot/by-ext [".jar"] files))))

(defn- shell [& args]
  (let [output (apply sh args)]
    (if (zero? (:exit output))
      identity
      (throw (Exception. (str "non-zero exit code: " output))))))

(boot/deftask dockerfile
  "Creates a generic Dockerfile at the root of the project.

  Note that your application may require a custom Dockerfile, in which case
  you shouldn't use this step at all.

  The generated Dockerfile loads a jar onto a Java 8 JRE image and sets the
  entrypoint to `java -jar the-jar.jar`."
  []
  (boot/with-pre-wrap fileset
    (util/info "Writing default Dockerfile...\n")
    (let [jar-path (find-jar (boot/output-files fileset))
          tmp (boot/tmp-dir!)
          dockerfile (io/file tmp "Dockerfile")]
      (spit-default-dockerfile dockerfile jar-path)
      (boot/commit! (boot/add-resource fileset tmp)))))

(boot/deftask docker-image
  "Builds a Docker image from the target/ path.

  Note that the image-name parameter is required.

  The target/ path is used in order to pair well with boot's built-in target
  task. Docker doesn't seem to play nicely with symlinks, so we ask that you
  write your build artifacts to target/ before trying to build a Docker image."
  [n image-name VAL str "name of the Docker image"]
  (boot/with-pass-thru [_]
    (util/info (format "Building %s Docker image...\n" image-name))
    (shell "docker" "build" "-t" image-name "target")))

(boot/deftask docker-tag
  "Tags an image's latest version with the given string.

  Note that the group-name, image-name, and tag parameters are required."
  [g group-name VAL str "name of the image's group"
   n image-name VAL str "name of the Docker image"
   t tag VAL str "string to use as tag"]
  (let [tag-name (str group-name "/" image-name ":" tag)]
    (boot/with-pass-thru [_]
      (util/info (format "Tagging %s Docker image as %s...\n)" image-name tag-name))
      (shell "docker" "tag" image-name tag-name))))

(boot/deftask docker-push
  "Logs into some Docker repository and pushes the given Docker image to it.

  Note that the group-name and image-name parameters are required.

  You must also set the environment variables DOCKER_REPOSITORY_USERNAME and
  DOCKER_REPOSITORY_PASSWORD accordingly."
  [g group-name VAL str "name of the image's group"
   n image-name VAL str "name of the Docker image"]
  (let [pushed-name (str group-name "/" image-name)
        username (System/getenv "DOCKER_REPOSITORY_USERNAME")
        password (System/getenv "DOCKER_REPOSITORY_PASSWORD")]
    (boot/with-pass-thru [_]
      (util/info (format "Pushing %s Docker image...\n" pushed-name))
      (shell "docker" "login" "-u" username "-p" password)
      (shell "docker" "push" pushed-name))))
