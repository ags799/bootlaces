# bootlaces
[![CircleCI](https://circleci.com/gh/ags799/bootlaces.svg?style=shield&circle-token=82acf73298c23c3e10fd6e1ac601cb4ccf153efc)](https://circleci.com/gh/ags799/bootlaces)
[![Clojars Project](https://img.shields.io/clojars/v/ags799/bootlaces.svg)](https://clojars.org/ags799/bootlaces)

Andrew Sharp's boot configurations for Clojure code.

Based on, but not a fork of,
[adzerk's bootlaces](https://github.com/adzerk-oss/bootlaces).

## Usage

This project provides a set of [boot](boot-clj.com) tasks for Clojure projects.
You can use them by requiring the project in your `build.boot` and calling
`bootlaces!`.

Here's an example `build.boot`:
```clojure
(set-env! :resource-paths #{"src"}
          :dependencies `[[org.clojure/clojure ~(clojure-version)]
                          [tolitius/boot-check "0.1.5"]])

(require '[org.clojars.ags799.bootlaces :refer :all])

; must call this with your Maven group ID and artifact ID
(bootlaces! 'your.group.id/your.artifact.id)
```

Consult each task's help output for more documentation.

## Development

Prepare your development environment by installing [boot](boot-clj.com).

Check your code against linters with

    boot check
