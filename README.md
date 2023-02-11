# µdeps
Mdeps, pronouced "micro deps",  is a micro dependencies management library.

## Micro what ?
A micro dependency is a **function**, whose source code is hosted by a **source**,  that you inject in a namespace. As an example, you will find in this repository an [edn file](https://github.com/Joeyjoejoe/micro-deps/blob/master/env/test/resources/hello-world.edn "edn file") which contains an `hello-world` function source code.

```edn
{:name "hello-world"
 :body "(fn [] \"Hello World!\")"}
```
Mdeps lets you use that function, let's find how !

## Installation
At the root of your project,  create or edit `deps.edn` file:
```clojure
{:deps
  {io.github.Joeyjoejoe/micro-deps {:git/tag "v1-alpha" :git/sha "09df7dc"}}}
```
## Usage
The `udeps.core` namespace provides a single macro `inject!`that accepts an unmilited amount of **namespaced keywords** and defines functions in its current namespace.
```clojure
user=> (require '[udeps.core :as udeps])
```
The keyword namespace is the **source** and its name the **identifier** `:source/identifier`. In order to inject the previous `hello-world` function in the current `user` namespace, use the **:http** source and **hello-world.edn** identifier, which forms the keyword `:http/hello-world.edn`
```clojure
user=> (udeps/inject! :http/hello-world.edn)
(µdeps) ✔ user #'user/hello-world :src/http
nil
```
The `hello-world` function has been defined in the `user` namespace:
```clojure
user=> (hello-world)
Hello World!
nil
```
Understanding how the keyword `:http/hello-world.edn` managed to reach the `hello-world` function hosted on this github repo is a matter of ...

## Configuration
