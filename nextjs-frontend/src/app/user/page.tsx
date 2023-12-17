"use client";
import { signOut, useSession } from "next-auth/react";
import "../../styles/user_user.scss";
import Image from "next/image";
import Link from "next/link";
import { useRouter } from "next/navigation";
export default function Home() {
  let InitData:any = {
    jwt: "",
    username: "",
    email:""
  }
  if (!localStorage.getItem("login")) {
    localStorage.setItem("login", JSON.stringify(InitData));
  }
  let data: any = localStorage.getItem("login");
  const ArrayData: any = JSON.parse(data);
  const router = useRouter();
  return (
    <>
      <p
        style={{
          maxWidth: "1300px",
          marginLeft: "auto",
          marginRight: "auto",
        }}
      >
        Trang chủ {">>"} Trang cá nhân
      </p>
      <div className="user_container">
        <div className="user_sidebar">
          <div className="user_image">
            <div className="image">
              <Image
                src={"/images/logo/avt1.png"}
                alt="Image"
                width={100}
                height={100}
                className="image_item"
              />
            </div>

            <div className="info mx-auto my-auto">
              <p className="uppercase font-bold">Tài khoản của: </p>
              <p className="flex items-center justify-center">user 1</p>
            </div>
          </div>
          <div className="sidebar_child common_info">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              height="1.2em"
              viewBox="0 0 448 512"
              className="mr-1"
            >
              <path d="M0 96C0 78.3 14.3 64 32 64H416c17.7 0 32 14.3 32 32s-14.3 32-32 32H32C14.3 128 0 113.7 0 96zM0 256c0-17.7 14.3-32 32-32H416c17.7 0 32 14.3 32 32s-14.3 32-32 32H32c-17.7 0-32-14.3-32-32zM448 416c0 17.7-14.3 32-32 32H32c-17.7 0-32-14.3-32-32s14.3-32 32-32H416c17.7 0 32 14.3 32 32z" />
            </svg>
            Thông tin chung
          </div>
          <div className="sidebar_child account hover:cursor-pointer">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              height="1.2em"
              viewBox="0 0 448 512"
              className="mr-1"
            >
              <path d="M224 256A128 128 0 1 0 224 0a128 128 0 1 0 0 256zm-45.7 48C79.8 304 0 383.8 0 482.3C0 498.7 13.3 512 29.7 512H418.3c16.4 0 29.7-13.3 29.7-29.7C448 383.8 368.2 304 269.7 304H178.3z" />
            </svg>
            Tài khoản
          </div>
          {/* <div className="sidebar_child noitice hover:cursor-pointer">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              height="1.2em"
              viewBox="0 0 512 512"
              className="mr-1"
            >
              <path d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zm0-384c13.3 0 24 10.7 24 24V264c0 13.3-10.7 24-24 24s-24-10.7-24-24V152c0-13.3 10.7-24 24-24zM224 352a32 32 0 1 1 64 0 32 32 0 1 1 -64 0z" />
            </svg>
            Thông báo
          </div> */}
          <div
            className="sidebar_child change_password hover:cursor-pointer"
            onClick={() => router.push(`/user/changepassword`)}
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              height="1.2em"
              viewBox="0 0 448 512"
              className="mr-1"
            >
              <path d="M144 144v48H304V144c0-44.2-35.8-80-80-80s-80 35.8-80 80zM80 192V144C80 64.5 144.5 0 224 0s144 64.5 144 144v48h16c35.3 0 64 28.7 64 64V448c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V256c0-35.3 28.7-64 64-64H80z" />
            </svg>
            Đổi mật khẩu
          </div>
          <div
            className="sidebar_child exit hover:cursor-pointer"
            onClick={() => {
              localStorage.removeItem('login');
              router.push('/');
            }}
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              height="1.2em"
              viewBox="0 0 512 512"
              className="mr-1"
            >
              <path d="M377.9 105.9L500.7 228.7c7.2 7.2 11.3 17.1 11.3 27.3s-4.1 20.1-11.3 27.3L377.9 406.1c-6.4 6.4-15 9.9-24 9.9c-18.7 0-33.9-15.2-33.9-33.9l0-62.1-128 0c-17.7 0-32-14.3-32-32l0-64c0-17.7 14.3-32 32-32l128 0 0-62.1c0-18.7 15.2-33.9 33.9-33.9c9 0 17.6 3.6 24 9.9zM160 96L96 96c-17.7 0-32 14.3-32 32l0 256c0 17.7 14.3 32 32 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32l-64 0c-53 0-96-43-96-96L0 128C0 75 43 32 96 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32z" />
            </svg>
            Thoát
          </div>
        </div>
        <div className="user_content ml-4">
          <div className="font-bold text-2xl">Thông tin chung</div>
          <div className="user_information flex relative">
            <div className="user_text flex justify-start gap-3">
              <div>
                <p>Tên đăng nhập</p>
                <p>Email</p>
              </div>
              <div className="">
                <p>{ArrayData?.username}</p>
                <p>{ArrayData?.email}</p>
              </div>
            </div>
            <Link href="/user/changeinformation" className="link_changeinfo">
              Thay đổi thông tin
            </Link>
          </div>
          <div className="follow_manga">
            <div>Truyện theo dõi</div>
            <Link
              href="/truyen-theo-doi"
              className="hover:cursor-pointer hover:text-blue-500"
            >
              xem tất cả {">"}
            </Link>
          </div>
          <div className="follow_list">
            <table></table>
          </div>
        </div>
      </div>
    </>
  );
}
