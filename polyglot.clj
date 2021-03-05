(def ctx
  (.build (org.graalvm.polyglot.Context/newBuilder (into-array String ["aml"]))))

(println
  (.asInt (.eval ctx "aml" "1/3 · 3;")))