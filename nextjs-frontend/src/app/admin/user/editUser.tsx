"use client";
import { useEffect, useState } from "react";
import "../../../styles/manga.scss";
export default function EditUser(props: any) {
  const [data, setData] = useState<any>();
  const [user, setUser] = useState<any>({
    avatar: "",
    email: "",
    full_name: "",
    role: "ROLE_USER",
    username: "",
  });
  const handleOnChange = (e: any) => {
    setUser({
      ...user,
      [e.target.name]: [e.target.value],
    });
  };
  useEffect(() => {
    setData(props.props);
  }, [props.props]);
  const handleUpdateData = async (id: any) => {
    alert("Chức năng đang phát triển");
    // const data = await fetch(`http://localhost:8080/api/user/${id}`, {
    //   method: "PUT",
    //   body: JSON.stringify(user),
    //   headers: {
    //     "Content-Type": "application/json",
    //   },
    // });
    // if (data) {
    //   console.log(data);
    //   alert("updated data");
    //   window.location.reload();
    // } else {
    //   alert("error");
    // }
  };
  return (
    <>
      {data && (
        <div className="pageEdit_container">
          <div className="page_content flex">
            <div className="mr-7">
              <p>id</p>
              <p>avatar</p>
              <p>email</p>
              <p>full_name</p>
              <p>role</p>
              <p>username</p>
            </div>
            <div>
              <p>{data.id}</p>
              <input
                type="text"
                placeholder={data.avatar}
                name="avatar"
                onChange={handleOnChange}
              />
              <br />
              <input
                type="email"
                placeholder={data.email}
                name="email"
                onChange={handleOnChange}
              />
              <br />
              <input
                type="text"
                placeholder={data.full_name}
                name="full_name"
                onChange={handleOnChange}
              />
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
                placeholder={data.username}
                className="mt-2"
                name="username"
                onChange={handleOnChange}
              />
            </div>
          </div>
          <div className="flex items-center justify-center">
            <button onClick={() => handleUpdateData(data.id)}>Save</button>
          </div>
        </div>
      )}
    </>
  );
}
