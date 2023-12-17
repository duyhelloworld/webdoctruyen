"use client";
import "../../styles/truyen-theo-dois.scss";
import Link from "next/link";
import Image from "next/image";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
export default function Home() {
  const [listFollow, setListFollow] = useState<any>(null);
  useEffect(() => {
    let data: any = localStorage.getItem("manga");
    const ArrayData: any = JSON.parse(data);
    setListFollow(ArrayData);
    console.log("ArrayData = ", ArrayData);
  }, []);

  const router = useRouter();
  return (
    <>
      <div className="ml-16 font-semibold text-blue-700 font text-2xl">
        Truyện mới cập nhật {">"}
      </div>
      <div className="followPage_container mx-auto mt-1 ml-1 gap-3">
        {listFollow &&
          listFollow.map((item: any, index: number) => {
            return (
              <div className="follow_item items-center" key={index}>
                <Link
                  href={`/truyen/${item.data.id}?chapterCount=${item.data.numberChapter}&filename=${item.data.image}`}
                  className="cursor-pointer"
                >
                  <img
                    src={`http://localhost:8080/api/book/cover-image?filename=${item.data.image}`}
                    alt={"#"}
                    className="image"
                    width={170}
                    height={180}
                  />
                  <div className="font-bold text-lg cursor-pointer max-w-prose break-words">
                    {item?.data.title}
                  </div>
                </Link>
              </div>
            );
          })}
      </div>
    </>
  );
}
