(ns metabase.models.revision.diff
  (:require [clojure.core.match :refer [match]]
            (clojure [data :as data]
                     [string :as s])))

(defn- diff-str* [t k v1 v2]
  (match [t k v1 v2]
    [_ :name _ _]
    (format "renamed it from \"%s\" to \"%s\"" v1 v2)

    [_ :private true false]
    "made it public"

    [_ :private false true]
    "made it private"

    [_ :updated_at _ _]
    nil

    [_ :dataset_query _ _]
    "modified the query"

    [_ :visualization_settings _ _]
    "changed the visualization settings"

    [_ _ _ _]
    (format "changed %s from \"%s\" to \"%s\"" (name k) v1 v2)))

(defn- build-sentence [parts]
  (when (seq parts)
    (cond
      (= (count parts) 1) (str (first parts) \.)
      (= (count parts) 2) (format "%s and %s." (first parts) (second parts))
      :else               (format "%s, %s" (first parts) (build-sentence (rest parts))))))

(defn diff-str
  ([t o1 o2]
   (let [[before after] (data/diff o1 o2)]
     (when before
       (let [ks (keys before)]
         (some-> (filter identity (for [k ks]
                                (diff-str* t k (k before) (k after))))
                 build-sentence
                 (s/replace-first #" it " (format " this %s " t)))))))
  ([username t o1 o2]
   (let [s (diff-str t o1 o2)]
     (when (seq s)
       (str username " " s)))))

(diff-str "Cam Saul" "card"
          {:name "Tips by State", :private false}
          {:name "Spots by State", :private false})
;; Cam Saul renamed this card from "Tips by State" to "Spots by State".

(diff-str "Cam Saul" "card"
          {:name "Spots by State", :private false}
          {:name "Spots by State", :private true})
;; Cam Saul made this card private.

(diff-str "Cam Saul" "card"
          {:name "Tips by State", :private false}
          {:name "Spots by State", :private true})
;; Cam Saul made this card private and renamed it from "Tips by State" to "Spots by State".

(diff-str "Cam Saul" "card"
            {:name "Tips by State", :private false, :priority "Important"}
            {:name "Spots by State", :private true, :priority "Regular"})
;; Cam Saul changed priority from "Important" to "Regular", made this card private and renamed it from "Tips by State" to "Spots by State".
