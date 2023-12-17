"use client";
import { useRouter } from "next/navigation";
import "../../../styles/user.scss";
import { useEffect, useState } from "react";
import EditUser from "./editUser";
export default function Home() {
  const router = useRouter();
  const [userData, setUserData] = useState<any>();
  const [editData, setEditData] = useState<any>();
  useEffect(() => {
    async function getdata() {
      const res: any = await fetch(`http://localhost:8080/api/user/all`).then(
        (res) => res.json()
      );
      setUserData(res);
    }
    getdata();
  }, []);
  return (
    <>
      <div className="user_container">
        <div className="relative overflow-x-auto user_table ">
          <table className="overflow-auto text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
            <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" className="px-6 py-3">
                  Id
                </th>
                <th
                  scope="col"
                  className="px-6 py-3"
                  style={{
                    width: "auto",
                  }}
                >
                  Username
                </th>
                <th
                  scope="col"
                  className="px-6 py-3"
                  style={{
                    width: "auto",
                  }}
                >
                  Email
                </th>
                <th
                  scope="col"
                  className="px-6 py-3"
                  style={{
                    width: "auto",
                  }}
                >
                  FullName
                </th>
                <th
                  scope="col"
                  className="px-6 py-3 text-center"
                  style={{
                    width: "auto",
                  }}
                >
                  Chức năng
                </th>
              </tr>
            </thead>
            <tbody>
              {userData &&
                userData.map((item: any, index: number) => {
                  return (
                    <tr
                      key={index}
                      className="bg-white border-b dark:bg-gray-800 dark:border-gray-700"
                    >
                      <th
                        scope="row"
                        className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
                      >
                        {item.id}
                      </th>
                      <td
                        className="px-6 py-4"
                        style={{
                          width: "auto",
                        }}
                      >
                        {item.username}
                      </td>
                      <td
                        className="px-6 py-4"
                        style={{
                          width: "auto",
                        }}
                      >
                        {item.email}
                      </td>
                      <td
                        className="px-6 py-4"
                        style={{
                          width: "auto",
                        }}
                      >
                        {item.fullName}
                      </td>
                      <td
                        className="px-6 py-4"
                        style={{
                          width: "auto",
                        }}
                      >
                        <button
                          className="edit"
                          onClick={() => setEditData(item)}
                        >
                          edit
                        </button>
                        <button className="remove" onClick={() => alert("Chức năng đang phát triển")}>remove</button>
                      </td>
                    </tr>
                  );
                })}
            </tbody>
          </table>
        </div>
        <div className="detail">
          <EditUser props={editData} />
        </div>
      </div>
    </>
  );
}
