import {UserSession} from "./model";

class UserSessionService{
    async getSessionByExamId(examId: number){
        const url = `/api/exam/${examId}/participants`
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Content-type": "application/json",
            },
        });
        let sessions: UserSession[] = [];
        if(response.ok){
            sessions = await response.json();
        }
        return sessions;
    }
}
const userSessionService = new UserSessionService();
export default userSessionService;