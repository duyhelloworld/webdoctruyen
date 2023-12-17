"use client";
import "../../../styles/manga.scss";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function Home() {
  const router = useRouter();
  const [user, setUser] = useState<any>({
    avatar: "",
    email: "",
    full_name: "",
    password: "",
    role: "ROLE_USER",
    username: "",
  });
  const handleOnChange = (e: any) => {
    setUser({
      ...user,
      [e.target.name]: [e.target.value],
    });
  };
  const handleCreateData = async () => {
    const res = await fetch(`http://localhost:8080/api/user`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    });
    if (res) {
      router.push("/user");
    }
    // console.log("user = ", user);
  };
  return (
    <>
      <div className="addmanga_container">
        <div className="add_page_content flex">
          <div className="mr-7">
            <p>avatar</p>
            <p>email</p>
            <p>full_name</p>
            <p>password</p>
            <p>role</p>
            <p>username</p>
          </div>
          <div>
            <input type="text" name="avatar" onChange={handleOnChange} />
            <br />
            <input type="email" name="email" onChange={handleOnChange} />
            <br />
            <input type="text" name="full_name" onChange={handleOnChange} />
            <br />
            <input type="password" name="password" onChange={handleOnChange} />
            <br />
            <select
              name="role"
              id=""
              onChange={handleOnChange}
              style={{
                border: "1px solid black",
                borderRadius: "5px",
              }}
            >
              <option value="ROLE_USER" selected>
                ROLE_USER
              </option>
              <option value="ROLE_ADMIN">ROLE_ADMIN</option>
            </select>
            <br />
            <input
              type="text"
              name="username"
              className="mt-2"
              onChange={handleOnChange}
            />
            <br />
            <button onClick={handleCreateData}>Save</button>
          </div>
        </div>
      </div>
    </>
  );
}
