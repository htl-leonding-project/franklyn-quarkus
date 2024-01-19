class FrameService {
    #link = "/streaming/api/live-image";

    async getImageForUser(testName: string, userName: string) {
        const url = `${this.#link}/${testName}/${userName}`
        console.info(url)
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Content-type": "application/json",
            },
        })
        if (response.ok) {
            console.log(await response.json())
            return await response.json() as { image: string }
        } else {
            console.log(response.statusText)

            return null;
        }
    }
}

const frameService = new FrameService()
export default frameService