package com.smartherd.googlelens

class SearchRvModel {
    var title:String
        get() {
            return title
        }
        set(value) {
            this.title = title

        }
    var link:String
        get() {
            return link
        }
        set(value) {
            this.link = link
        }
    private var displayed_link:String
        get() {
            return displayed_link
        }
        set(value) {
            this.displayed_link = displayed_link
        }
    var snippet:String
        get() {
            return snippet
        }
        set(value) {
            this.snippet = snippet
        }


    constructor(title: String, link: String, displayed_link: String, snippet: String) {
        this.title = title
        this.link = link
        this.displayed_link = displayed_link
        this.snippet = snippet

    }
}