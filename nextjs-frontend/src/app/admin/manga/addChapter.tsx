"use client";

import {useState} from "react";
import axios from "axios";
export default function AddChapter(props: any) {
    const [images, setImages] = useState<any>([]);
    const [jsonData, setJsonData] = useState({
        title: "",
        bookId: props.props.id
    });

    const handleImageChange = (e:any) => {
        // Handle image file selection
        const selectedImages = Array.from(e.target.files);
        setImages(selectedImages);
    };

    const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      setJsonData({ ...jsonData, title: e.target.value });
    };

    const handleSubmit = async () => {
        try {
            const formData = new FormData();

            // Append images to FormData
            images.forEach((image:any, index:number) => {
                formData.append(`files`, image);
            });
            console.log("Json : " + jsonData);
            // Append JSON data to FormData
            formData.append('model', JSON.stringify(jsonData));

            // Send POST request to the server
            const response = await axios.post('http://localhost:8080/api/chapter', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            if(response.status === 200) {
                alert("Thêm thành công " + response.data.title);
            }
            // Handle the response as needed
            console.log(response.data);
        } catch (error) {
          let msg = error as any;
          if ((error as any).response) {
            msg = (error as any).response.data.message;
          }
          alert('Lỗi thêm chapter: ' + msg);
        }
    };

  return (
    <>
      <div className="add_chapter_container">
        <div>Thêm chapter cho sách : {props.props.title}</div>
        <input type="text" placeholder="Chapter Title" value={jsonData.title} onChange={handleTitleChange} />
          <input type="file" multiple onChange={handleImageChange} />
          <button style={{color: "black"}} onClick={handleSubmit}>Submit</button>
      </div>
    </>
  );
}
