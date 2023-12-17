"use client";
import "../../../styles/manga.scss";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import EditManga from "./editManga";
import AddChapter from "./addChapter";
export default function Home() {
  const router = useRouter();
  const [mangaData, setMangaData] = useState<any>();
  const [editData, setEditData] = useState<any>();
  const [isUpdate, setIsUpdate] = useState<boolean>(false);
  const [isCreate, setIsCreate] = useState<boolean>(false);
  useEffect(() => {
    async function getdata() {
      const res = await fetch(`http://localhost:8080/api/book/all?page=1`).then((res) =>
        res.json()
      );
      setMangaData(res);
    }
    getdata();
  }, []);
  const handleRemove = async (id: any) => {
    const data = await fetch(`http://localhost:8080/api/book/${id}`, {
      method: "DELETE", // Method itself
      headers: {
        "Content-type": "application/json; charset=UTF-8", // Indicates the content
      },
    });
    if (data) {
      console.log(data);
      alert("deleted data");
      window.location.reload();
    } else {
      alert("error");
    }
  };
  return (
    <>
      <div className="manga_container flex">
        <div className="relative overflow-x-auto container_table">
          <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
            <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" className="px-6 py-3">
                  Id sách
                </th>
                <th
                  scope="col"
                  className="px-6 py-3"
                  style={{
                    minWidth: "200px",
                    width: "auto",
                  }}
                >
                  Tên sách
                </th>
                <th scope="col" className="px-6 py-3 text-center">
                  function
                </th>
              </tr>
            </thead>
            <tbody>
              {mangaData &&
                mangaData.map((item: any, index: number) => {
                  return (
                    <tr
                      className="bg-white border-b dark:bg-gray-800 dark:border-gray-700"
                      key={index}
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
                          maxWidth: "200px",
                          whiteSpace: "nowrap",
                          overflow: "auto",
                        }}
                        colSpan={1}
                      >
                        {item.title}
                      </td>

                      <td className="px-6 py-4">
                        <button
                          className="edit"
                          onClick={() => {
                            setEditData(item);
                            setIsUpdate(true);
                          }}
                        >
                          edit
                        </button>
                        <button
                          className="remove"
                          onClick={() => handleRemove(item.id)}
                        >
                          remove
                        </button>
                        <button
                          className="add_chapter"
                          onClick={() => {
                            setEditData(item);
                            setIsUpdate(false);
                            setIsCreate(true);
                          }}
                        >
                          add chapter
                        </button>
                      </td>
                    </tr>
                  );
                })}
            </tbody>
          </table>
        </div>
        <div className="detail">
          {isUpdate ? (
            <EditManga props={editData} />
          ) : isCreate ? (
            <AddChapter props={editData} />
          ) : (
            <div></div>
          )}
        </div>
      </div>
    </>
  );
}
