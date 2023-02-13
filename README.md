# µdeps
Mdeps, pronouced "micro deps",  is a micro dependencies management library.

### Micro what ?
A micro dependency is a **function** you use in your program, but whose source code is outside your program.
On this github repository, there's this .edn file which contains data of an `#'hello-world` function, check it out:
https://raw.githubusercontent.com/Joeyjoejoe/udeps/master/env/test/resources/hello-world.edn

This is a micro dependency and µdeps lets you use that function in your program. Let's find how !

## Installation
Add this to you deps.edn `:deps` map
```
io.github.Joeyjoejoe/micro-deps {:git/tag "v1-alpha" :git/sha "09df7dc"}
```
## Usage
The `udeps.core` namespace provides a single macro `inject!`, that is used to inject a function in the current namespace.
```clojure
user=> (require '[udeps.core :as udeps])
```
To indicate which function to inject, µdeps uses namespaced keywords of this pattern: `:source/identifier`
As an example, the keyword for `#'hello-world` function hosted [here](https://raw.githubusercontent.com/Joeyjoejoe/udeps/master/env/test/resources/hello-world.edn) is `:remote/hello-world.edn`.

It's composed of the `:remote` **source** and `:hello-world.edn` **identifier**. Let's inject the `#'hello-world` function in the current namespace:
```clojure
user=> (udeps/inject! :remote/hello-world.edn)
(µdeps) user ✔ :remote/hello-world.edn #'user/hello-world :src/remote
nil
```
The `hello-world` function has been defined in the `user` namespace:
```clojure
user=> (hello-world)
Hello World!
nil
```
Understanding how the keyword `:remote/hello-world.edn` managed to reach the `hello-world` function hosted on this github repo is a matter of...
## Configuration
Mdeps will search for a `udeps.edn` resource in your classpath, but the default configuration provide good explanations:
```edn
{:cfg/verbose    true
 :cfg/cache?     true
 :cfg/cache-path ".udeps-cache/"

 ;;   Search and read a file at configured path,
 ;;   keyword's identifier is the filename:
 ;;     example: :local/foo.edn
 ;;              => .udeps/foo.edn

 :src/local  ".udeps/"


 ;;   Perform an HTTP get query that return a function's data map.
 ;;     example:
 ;;     :http/hello-world.edn
 ;;     => GET https://raw.githubu...nv/test/hello-world.edn

 :src/remote {:url     "https://raw.githubusercontent.com/Joeyjoejoe/micro-deps/master/env/test/resources/"
              :headers {"Accept" "*/*"}
              :timeout 1000
              ;; :as      :auto
              ;; :basic-auth ["user" "pass"]
              ;; :query-params {:param "value" :param2 ["value1" "value2"]}
              ;; :user-agent "User-Agent-string"
              }}

```



