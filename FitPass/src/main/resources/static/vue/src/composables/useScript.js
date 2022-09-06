import {onUnmounted} from "vue";

export default function (src) {
    return new Promise((resolve, reject) => {
        let script = document.querySelector(`script[src="${src}"]`)

        if (!script) {
            script = document.createElement("script")
            script.src = src
            script.async = true
            script.setAttribute("data-status", "loading")
            document.head.append(script)
        }

        if (script.getAttribute("data-status") === "loaded") {
            resolve()
        }

        function onScriptLoad() {
            resolve()
            script.setAttribute("data-status", "loaded")
        }

        function onScriptError() {
            reject()
            script.setAttribute("data-status", "error")
        }

        script.addEventListener("load", onScriptLoad)

        script.addEventListener("error", onScriptError)

        let list = document.getElementsByTagName('script');

        onUnmounted(() => {
            if (document.head.contains(script)) {
                script.removeEventListener("load", onScriptLoad)
                script.removeEventListener("error", onScriptError)
                document.head.removeChild(script)
            }

            let i = list.length
            while (i--) {
                if (list[i].src.startsWith("https://maps.googleapis.com/")) {
                    document.head.removeChild(list[i])
                }
            }
        })
    })
}