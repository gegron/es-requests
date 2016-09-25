import groovy.json.JsonOutput

Locale.setDefault(Locale.ENGLISH)

def result = new IntRange(1, 24).collect {
    new URL("http://blog.xebia.fr/feed/?paged=$it").text
}
.collect {
    new XmlSlurper().parseText(it).channel.item
            .collect {
        ["title"      : it.title.toString(),
         "pubDate"    : Date.parse("EEE, dd MMM yyyy HH:mm:ss Z", it.pubDate.toString()).format("yyyy-MM-dd'T'HH:mm:ss"),
         "creator"    : it.creator.toString(),
         "category"   : it.category.collect { it.toString() },
         "description": it.description.toString(),
         "content"    : it.encoded.toString()
        ]
    }
}
.flatten()
        .collect {
    "{\"index\" : {}}\n" + JsonOutput.toJson(it)
}.join("\n")

def file = new File("xebiablog.data")
file.delete()
file << result
