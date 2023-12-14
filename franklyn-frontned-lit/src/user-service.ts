import {User} from "./model";

class UserService {
  #url1 = "/api/user/all";
  //#url2 = "https://jsonplaceholder.typicode.com/todos";
  async getAll() {
    let users: User[];

    const response = await fetch(this.#url1, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
      },
    });

    //console.log(response.statusText, response.status);
    if (response.ok) {
      users = await response.json();
    }
    console.log(users);
    return users;
  }
}

const userService = new UserService();
export {userService};
