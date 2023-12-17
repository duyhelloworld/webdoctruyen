"use client";
import "../../../styles/manga.scss";
import { useRouter } from "next/navigation";
import {useEffect, useState} from "react";
import axios from "axios";


export default function Home() {
  const [token, setToken] = useState<any>();
  useEffect(() => {
    let data: any = localStorage.getItem("login");
    const ArrayData: any = JSON.parse(data);
    // console.log("arrayData: " , ArrayData.jwt);
    setToken(ArrayData.jwt)
  }, []);
  token && console.log("token = ", token);
  // const router = useRouter();
  const [manga, setManga] = useState<any>({
    title: "",
    description: "",
    releaseDate: "",
    author: "",
  });
  const handleOnChange = (e: any) => {
    setManga({
      ...manga,
      [e.target.name]: [e.target.value],
    });
  };
  const handleCreateData = async () => {
    const data:any = {
      title: manga.title[0],
      description: manga.description[0],
      releaseDate: manga.releaseDate[0],
      author: manga.author[0],
    }
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
    try {
      const res = await axios.post(`http://localhost:8080/api/book`, data,
          {headers: headers});
      if (res.status == 200) {
        console.log("res = ", res);
        alert("Thêm truyện mới thành công")
        // router.push("/admin/manga");
      }
    }
    catch (e){
      alert("Lỗi")
    }
      console.log("data = ", data)
  };
  return (
    <>
      <div className="addmanga_container">
        <div className="add_page_content flex">
          <div className="mr-7">
            <p>Tên truyện</p>
            <p>Mô tả</p>
            <p className="mt-28">Ngày đăng</p>
            <p>Tác giả</p>
          </div>
          <div>
            <input type="text" name="title" onChange={handleOnChange} />
            <br />
            <textarea
              rows={5}
              cols={22}
              name="description"
              onChange={handleOnChange}
            />
            <br />
            
            <input
              type="date"
              className="mt-2"
              name="releaseDate"
              onChange={handleOnChange}
            />
            
            <br />
            <input type="text" name="author" onChange={handleOnChange} />
            <br />
            <button onClick={handleCreateData}>Save</button>
          </div>
        </div>
      </div>
    </>
  );
}