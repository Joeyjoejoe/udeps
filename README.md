# µdeps

Mdeps is a micro dependencies management library. A micro dependency
is a **function** obtained from a distant **source** and injected into a
namespace.

## Installation

Add this line to your deps.edn file:

```clojure
{:deps
  {io.github.Joeyjoejoe/micro-deps {:git/tag "v1-alpha" :git/sha "09df7dc"}}}
```

## Usage

Mdeps provide

```clojure
(ns some.ns
  (:require [udeps.core :as udeps])

;; Inject bar **function** from the :folder **source** into `some.ns` namespace
(udeps/inject! :folder/bar)
```

## Configuration

Mdeps can be configured by adding a udeps.edn file to your resource path:

```edn
{:paths ["src" "config"]
 :deps
   {org.clojure/clojure {:mvn/version "1.11.1"}
    io.github.Joeyjoejoe/micro-deps {:git/tag "v1-alpha" :git/sha "09df7dc"}}}
```
