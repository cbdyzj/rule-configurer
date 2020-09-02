// hello
(function (input) {
    const target = JSON.parse(input) || {}
    if (!target.id) {
        return null
    }
    return `hello ${target.name}`.trim()
})(input)
