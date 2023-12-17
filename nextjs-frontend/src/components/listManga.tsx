"use client";
import axios from "axios";
import "../styles/testss.scss";
import Link from "next/link";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function ListManga(props: any) {
  console.log(props.props);
  const router = useRouter();
  const [dataManga, setDataManga] = useState<any>(null);

  useEffect(() => {
    const getManga = async () => {
      const res = await axios.get(`http://localhost:8080/api/book/all?page=${props.props}`);
      const listManga = await res.data;
      setDataManga(listManga);
      console.log(res.data);
    };
    getManga();
  }, [props.props]);
  // dataManga && console.log("dataManga = ", dataManga);
  return (
    <>
      <div className="ml-1 font-semibold text-blue-700 font text-2xl">
        Truyện mới cập nhật {">"}
      </div>
      <div className="list_container mx-auto mt-1 ml-1 gap-3">
        {dataManga &&
          dataManga.map((item: any, index: number) => {
            let chapter = JSON.stringify(item.chapters);
            let arrChapter = JSON.parse(chapter);
            let len = arrChapter.length;
            return (
              <div className="list_item items-center" key={index}>
                <Link href={`/truyen/${item.id}?chapterCount=${len}&filename=${item.coverImage}`} className="cursor-pointer">
                  <img
                    src={`http://localhost:8080/api/book/cover-image?filename=${item.coverImage}`}
                    alt={"#"}
                    className="image_render"
                    width={170}
                    height={180}
                  />
                  <p
                    className="manga_title font-bold text-base cursor-pointer"
                    onClick={() =>
                      router.push(
                        `/truyen/${item.id}?chapterCount=${len}&filename=${item.coverImage}`
                      )
                    }
                  >
                    {item?.title}
                  </p>
                </Link>
                <p className="italic cursor-pointer">{len}</p>
                <p className="italic cursor-pointer">{len == 0 ? (<>...</>) : len - 1}</p>
                <p className="italic cursor-pointer">...</p>
              </div>
            );
          })}

      </div>
    </>
  );
}
