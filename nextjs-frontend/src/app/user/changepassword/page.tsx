"use client";
import "../../../styles/user_user.scss";
import { useEffect, useState } from "react";
import Image from "next/image";
export default function Home() {
  const [pass, setPass] = useState<any>({
    oldPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const [token, setToken]  = useState<any>();
  useEffect(() => {
    let data: any = localStorage.getItem("login");
    const ArrayData: any = JSON.parse(data);
    // console.log("arrayData: " , ArrayData.jwt);
    setToken(ArrayData.jwt)
  })
  
  const handleChangePassword = async () => {
    const data = {
      oldPassword: pass.oldPassword[0],
      newPassword: pass.newPassword[0],
      confirmPassword: pass.confirmPassword[0]
    }
    try {        
      const res = await fetch('http://localhost:8080/api/auth/change-password', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(data),
      });
      if (res.status == 200) {
        alert("Đổi mật khẩu thành công");
      }
    } catch (error) {
      alert(`${(error as any).response.data.message}\n${(error as any).response.data.solution}`);
    }
  };
  const handleChangeValue = (e: any) => {
    setPass({
      ...pass,
      [e.target.name]: [e.target.value],
    });
  };
  return (
    <>
      <div className="change_container mx-auto">
        <div className="flex items-center justify-center">
          <div>
            <p className="mb-3">Mật khẩu hiện tại</p>
            <p className="mb-3">Mật khẩu mới</p>
            <p>Nhập lại mật khẩu mới</p>
          </div>
          <div className=" ml-3">
            <input
              className="mb-3"
              type="password"
              name="oldPassword"
              onChange={handleChangeValue}
            />
            <br />
            <input
              type="password"
              className="mb-3"
              onChange={handleChangeValue}
              name="newPassword"
            />
            <br />
            <input
              type="password"
              name="confirmPassword"
              onChange={handleChangeValue}
            />
          </div>
        </div>
        <div className="button_submit" onClick={handleChangePassword}>
          submit
        </div>
      </div>
    </>
  );
}
