"use client";
import { useEffect, useState } from "react";
import "../../../styles/manga.scss";
export default function EditManga(props: any) {
  const [data, setData] = useState<any>();
  const [manga, setManga] = useState<any>({
    title: "",
    description: "",
    release_date: "",
    upload_at: "",
    cover_image: "",
    author: "",
  });
  const handleOnChange = (e: any) => {
    setManga({
      ...manga,
      [e.target.name]: [e.target.value],
    });
  };
  useEffect(() => {
    setData(props.props);
  }, [props.props]);
  const handleUpdateData = async (id: any) => {
    const data = await fetch(`http://localhost:8000/api/manga/${id}`, {
      method: "POST",
      body: JSON.stringify(manga),
      headers: {
        "Content-Type": "application/json",
      },
    });
    if (data) {
      console.log(data);
      alert("updated data");
      window.location.reload();
    } else {
      alert("error");
    }
  };
  return (
    <>
      {data && (
        <div className="pageEdit_container">
          <div className="page_content flex">
            <div className="mr-7">
              <p>id</p>
              <p>title</p>
              <p>description</p>
              <p className="mt-28">release_date</p>
              <p>upload_at</p>
              <p>cover_image</p>
              <p>author</p>
            </div>
            <div>
              <p>{data.id}</p>
              <input
                type="text"
                placeholder={data.title}
                name="title"
                onChange={handleOnChange}
              />
              <br />
              <textarea
                rows={5}
                cols={22}
                placeholder={data.description}
                name="description"
                onChange={handleOnChange}
              />
              <br />
              <input
                type="date"
                className="mt-2"
                placeholder={data.release_date}
                name="release_date"
                onChange={handleOnChange}
              />
              <br />
              <input
                type="date"
                placeholder={data.upload_at}
                name="upload_at"
                onChange={handleOnChange}
              />
              <br />
              <input
                type="text"
                placeholder={data.cover_image}
                name="cover_image"
                onChange={handleOnChange}
              />
              <br />
              <input
                type="text"
                placeholder={data.author}
                name="author"
                onChange={handleOnChange}
              />
            </div>
          </div>
          <div className="flex items-center justify-center">
            <button onClick={() => handleUpdateData(data.id)}>Save</button>
            {/* <button onClick={() => setData("")}>cancel</button> */}
          </div>
        </div>
      )}
    </>
  );
}
