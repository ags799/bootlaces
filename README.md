# bootlaces
[![CircleCI](https://circleci.com/gh/ags799/bootlaces.svg?style=shield&circle-token=82acf73298c23c3e10fd6e1ac601cb4ccf153efc)](https://circleci.com/gh/ags799/bootlaces)
[![Clojars Project](https://img.shields.io/clojars/v/ags799/bootlaces.svg)](https://clojars.org/ags799/bootlaces)

Andrew Sharp's boot configurations for Clojure code.

Based on, but not a fork of,
[adzerk's bootlaces](https://github.com/adzerk-oss/bootlaces).

## Usage

This project provides a set of [boot](boot-clj.com) tasks for Clojure projects.
You can use them by requiring the project in your `build.boot` and calling
`bootlaces!` with the requisite parameters.

View an example project that utilizes this repo
[here](https://github.com/ags799/clojure-example).

## Development

Prepare your development environment by installing [boot](boot-clj.com).

Verify your code's correctness and quality with

    boot verify
