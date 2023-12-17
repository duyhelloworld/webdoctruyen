"use client";
import "../styles/headers.scss";
import Image from "next/image";
import Link from "next/link";
import { useRouter } from "next/navigation";

export default function Header() {
  const router = useRouter();
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
  
  const handleLogout = () => {
    localStorage.removeItem("login");
    window.location.reload();
  }
  return (
    <>
      <div className="hed flex mx-auto items-center">
        <Link className="head_logo" href={"/"}>
          <Image
            src={"/images/logo/Logo.png"}
            alt={"truyen"}
            width={100}
            height={100}
            className="rounded cursor-pointer"
          />
        </Link>
        <div className="flex head_middle items-center ml-52">
          <div className="flex head_search mx-0.5 bg-amber-50 rounded">
            <input type="text" className="" placeholder=" Tìm truyện... " />
            <button>
              <Image
                src={"/images/logo/btn_search.png"}
                alt={"search"}
                width={30}
                height={30}
              />
            </button>
          </div>

          <div className="flex head_light_mes ml-7">
            <div className="mr-3.5 cursor-pointer">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                height="1.7em"
                viewBox="0 0 384 512"
                className="head_light"
              >
                <path d="M297.2 248.9C311.6 228.3 320 203.2 320 176c0-70.7-57.3-128-128-128S64 105.3 64 176c0 27.2 8.4 52.3 22.8 72.9c3.7 5.3 8.1 11.3 12.8 17.7l0 0c12.9 17.7 28.3 38.9 39.8 59.8c10.4 19 15.7 38.8 18.3 57.5H109c-2.2-12-5.9-23.7-11.8-34.5c-9.9-18-22.2-34.9-34.5-51.8l0 0 0 0c-5.2-7.1-10.4-14.2-15.4-21.4C27.6 247.9 16 213.3 16 176C16 78.8 94.8 0 192 0s176 78.8 176 176c0 37.3-11.6 71.9-31.4 100.3c-5 7.2-10.2 14.3-15.4 21.4l0 0 0 0c-12.3 16.8-24.6 33.7-34.5 51.8c-5.9 10.8-9.6 22.5-11.8 34.5H226.4c2.6-18.7 7.9-38.6 18.3-57.5c11.5-20.9 26.9-42.1 39.8-59.8l0 0 0 0 0 0c4.7-6.4 9-12.4 12.7-17.7zM192 128c-26.5 0-48 21.5-48 48c0 8.8-7.2 16-16 16s-16-7.2-16-16c0-44.2 35.8-80 80-80c8.8 0 16 7.2 16 16s-7.2 16-16 16zm0 384c-44.2 0-80-35.8-80-80V416H272v16c0 44.2-35.8 80-80 80z" />
              </svg>
            </div>
            <div className="head_mes cursor-pointer">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                height="1.7em"
                viewBox="0 0 512 512"
              >
                <path d="M160 368c26.5 0 48 21.5 48 48v16l72.5-54.4c8.3-6.2 18.4-9.6 28.8-9.6H448c8.8 0 16-7.2 16-16V64c0-8.8-7.2-16-16-16H64c-8.8 0-16 7.2-16 16V352c0 8.8 7.2 16 16 16h96zm48 124l-.2 .2-5.1 3.8-17.1 12.8c-4.8 3.6-11.3 4.2-16.8 1.5s-8.8-8.2-8.8-14.3V474.7v-6.4V468v-4V416H112 64c-35.3 0-64-28.7-64-64V64C0 28.7 28.7 0 64 0H448c35.3 0 64 28.7 64 64V352c0 35.3-28.7 64-64 64H309.3L208 492z" />
              </svg>
              <ul className="options">
                <li>
                  Unknow: Đã trả lời bình luận của bạn về truyện Grand Blue
                </li>
                <li>
                  Unknow2: Đã trả lời bình luận của bạn về truyện Frieren no
                  sounen
                </li>
              </ul>
            </div>
          </div>
        </div>
        {ArrayData?.jwt != "" ? (
            <div className="flex">
              <div className="hover-container">
              <button className="show_ul bg-gray-800 hover:bg-gray-700 text-white font-bold py-1 px-3 border border-gray-700 hover:border-transparent rounded">
                hi,{ArrayData.username}
              </button>

              <ul className=" px-3 options">
                <li onClick={() => router.push("/user")}>Trang cá nhân</li>
                <li onClick={() => router.push("/truyen-theo-doi")}>Theo dõi</li>
                <li onClick={handleLogout}>Đăng xuất</li>
              </ul>
            </div>
              <button className="bg-gray-800 hover:bg-gray-700 text-white font-bold py-1 px-3 border border-gray-700 hover:border-transparent rounded"
                      onClick={() => router.push('/admin/manga')}>
                admin
              </button>
            </div>

        ) : (
          <>
            <button
              type="button"
              className="text-black font-bold bg-green-600 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 rounded-lg text-lg px-4 py-2 text-center mr-3 md:mr-0 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
              onClick={() => router.push('/auth')}
            >
              Đăng nhập
            </button>
          </>
        )}
      </div>
      <div className="navbar flex mx-auto px-44 items-center justify-around bg-gray-200">
        <div className="navbar_item navbar_home flex items-center justify-center">
          <svg
            onClick={() => window.location.href = "http://localhost:3000"}
            xmlns="http://www.w3.org/2000/svg"
            height="1.5em"
            viewBox="0 0 576 512"
          >
            <path d="M575.8 255.5c0 18-15 32.1-32 32.1h-32l.7 160.2c0 2.7-.2 5.4-.5 8.1V472c0 22.1-17.9 40-40 40H456c-1.1 0-2.2 0-3.3-.1c-1.4 .1-2.8 .1-4.2 .1H416 392c-22.1 0-40-17.9-40-40V448 384c0-17.7-14.3-32-32-32H256c-17.7 0-32 14.3-32 32v64 24c0 22.1-17.9 40-40 40H160 128.1c-1.5 0-3-.1-4.5-.2c-1.2 .1-2.4 .2-3.6 .2H104c-22.1 0-40-17.9-40-40V360c0-.9 0-1.9 .1-2.8V287.6H32c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4 8c7-7 15-8 22-8s15 2 21 7L564.8 231.5c8 7 12 15 11 24z" />
          </svg>
        </div>
        <div className="navbar_item">HÀNH ĐỘNG</div>
        <div className="navbar_item">HÀI HƯỚC</div>
        <div className="navbar_item">PHIÊU LƯU</div>
        <div className="navbar_item hover_pseudo1 ">
          <div className="flex items-center">
            VÕ THUẬT
            {/* <div className="arrow-down"></div> */}
          </div>
          {/* <ul className="navbar_option1">
            <li className="option_item">Tất cả</li>
            <li className="option_item">Adult</li>
            <li className="option_item">Isekai</li>
            <li className="option_item">Comedy</li>
            <li className="option_item">Comic</li>
            <li className="option_item">Cooking</li>
            <li className="option_item">Manga</li>
            <li className="option_item">Manhua</li>
            <li className="option_item">Mecha</li>
            <li className="option_item">Drama</li>
          </ul> */}
        </div>
        <div className="navbar_item hover_pseudo2">
          <div className="flex items-center">
            HUYỀN BÍ
            {/* <div className="arrow-down"></div> */}
          </div>
          {/* <ul className="navbar_option2">
            <li className="option_item">Tất cả</li>
            <li className="option_item">Adult</li>
            <li className="option_item">Isekai</li>
            <li className="option_item">Comedy</li>
            <li className="option_item">Comic</li>
            <li className="option_item">Cooking</li>
            <li className="option_item">Manga</li>
            <li className="option_item">Manhua</li>
            <li className="option_item">Mecha</li>
            <li className="option_item">Drama</li>
          </ul> */}
        </div>
        <div className="navbar_item">TRINH THÁM</div>
        <div className="navbar_item">VIỄN TƯỞNG</div>
        <div className="navbar_item">DỊ GIỚI</div>
      </div>
    </>
  );
}
