"use client";
import "../../../styles/user_user.scss";
import { useEffect, useState } from "react";
import Image from "next/image";
export default function Home() {
  const [avatar, setAvatar] = useState<any>(null);
  const handleChangeImage = (e: any) => {
    const file = e.target.files[0];
    file.preview = URL.createObjectURL(file);
    setAvatar(file);
  };
  useEffect(() => {
    return () => {
      avatar && URL.revokeObjectURL(avatar.preview);
    };
  }, [avatar]);
  const handleChangeInfo = () => {
  };
  return (
    <>
      <div className="change_container mx-auto">
        <div className="flex items-center justify-center">
          <div>
            <p className="mb-3">Tên đăng nhập</p>
            <p>Email</p>
          </div>
          <div className="ml-3">
            <input className="mb-3" type="text" placeholder="Tên đăng nhập" />
            <br />
            <input type="text" placeholder="Email" />
          </div>
        </div>
        <div className="changeImage">
          <div className="imagePreview">
            <Image
              src={avatar != null ? avatar.preview : "/images/logo/avt1.png"}
              alt=""
              width={100}
              height={100}
            />
          </div>
          <div className="button-wrap">
            <label className="button" htmlFor="upload">
              Upload File
            </label>
            <input id="upload" type="file" onChange={handleChangeImage} />
          </div>
        </div>
        <div className="button_submit" onClick={handleChangeInfo}>
          submit
        </div>
      </div>
    </>
  );
}
