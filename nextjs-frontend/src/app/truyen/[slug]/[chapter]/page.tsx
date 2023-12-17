"use client";
import { useEffect, useState } from "react";
import "../../../../styles/truyens.scss";
import { useRouter } from "next/navigation";
import { useSearchParams } from "next/navigation";
import axios from "axios";
import Comment from "../../comment";
export default function Home(props: any) {
  const searchParams = useSearchParams();
  const bookId = searchParams.get("bookId");
  const chapterid = searchParams.get("chapterId");
  const title = searchParams.get("title");
  const [t, getT] = useState<any>();
  const [imageUrls, setImageUrls] = useState([null]);
  const [numberChapters, setNumberChapters] = useState<any>(null);
  const [selectedOption, setSelectedOption] = useState<any>();
  const [max, setMax] = useState<any>();
  const [min, setMin] = useState<any>();
  useEffect(() => {
    setSelectedOption(`${chapterid}`);
    const fetchData = async () => {
      const newImageUrls: any[] = [];
      try {
        const getBookById = await axios(
          `http://localhost:8080/api/book/${bookId}`
        );
        const chapters = await getBookById.data.chapters;
        setMax(Math.max(...chapters));
        setMin(Math.min(...chapters));
        setNumberChapters(chapters);
        getT(getBookById.data.title)
        console.log(getBookById.data);

        const getNumberImageChapter = await axios.get(
          `http://localhost:8080/api/chapter/${chapterid}`
        );
        for (let i = 0; i < getNumberImageChapter.data.numberOfImage; i++) {
          newImageUrls.push(
            `http://localhost:8080/api/chapter?chapterId=${chapterid}&fileId=${
              i + 1
            }`
          );
        }
        setImageUrls(newImageUrls);
      } catch (error) {
        console.log("failed fetching image");
      }
    };
    fetchData();
  }, [chapterid]);
  const handleSelectChange = (e: any) => {
    setSelectedOption(e.target.value);
    router.push(
      `/truyen/${title}/${props.params.chapter}?title=${title}&bookId=${bookId}&chapterId=${e.target.value}`
    );
  };
  const router = useRouter();
  const elements: JSX.Element[] = [];
  numberChapters &&
    numberChapters.map((item: any, index: number) => {
      elements.push(
        <option className="option" key={index} value={`${item}`}>
          Chương {index + 1}
        </option>
      );
    });
  return (
    <>
      <div className="w-full h-max">
        <div className="read_container">
          <div className="font-bold text-lg">
            Trang chủ / Manga / {t}
          </div>
          <p className="mx-auto flex justify-center items-center">
            Nếu không xem được truyện, vui lòng tải lại trang hoặc chuyển server
          </p>
          <div className=" read_content flex justify-center items-center mt-3">
            <button className="bg-green-500">Server 1</button>
            <button className="bg-blue-500 hover:bg-green-500">Server 2</button>
            <button className="bg-blue-500 hover:bg-red-500">Báo lỗi</button>
          </div>
          <p className="mx-auto flex justify-center items-center">
            Sử dụng mũi tên trái {"(<--)"} hoặc phải {"(-->)"} để chuyển chapter
          </p>
          <div className="flex justify-center items-center mt-3">
            <button
              style={{
                border: "1px solid black",
                borderRadius: "10px",
                width: "150px",
                height: "45px",
              }}
              className="bg-blue-500 hover:bg-green-500"
              onClick={() =>
                Number(chapterid) > min
                  ? router.push(
                      `/truyen/${title}/${
                        props.params.chapter
                      }?title=${title}&bookId=${bookId}&chapterId=${
                        Number(chapterid) - 1
                      }`
                    )
                  : null
              }
            >
              {"<--"}Chap trước
            </button>
            <button
              style={{
                border: "1px solid black",
                borderRadius: "10px",
                width: "150px",
                height: "45px",
                marginLeft: "12px",
              }}
              className="bg-blue-500 hover:bg-green-500"
              onClick={() =>
                Number(chapterid) < max
                  ? router.push(
                      `/truyen/${title}/${
                        props.params.chapter
                      }?title=${title}&bookId=${bookId}&chapterId=${
                        Number(chapterid) + 1
                      }`
                    )
                  : null
              }
            >
              Chap sau{"-->"}
            </button>
          </div>
        </div>

        <div className="read_content_manga">
          {imageUrls != null &&
            imageUrls.map((item: any, index: any) => {
              return (
                <div key={index}>
                  <img src={`${item}`} alt="" width={100} height={100} />
                  <br />
                </div>
              );
            })}
          <div>
            <Comment props={chapterid}/>
          </div>
        </div>

        <div className="read_control">
          <div>
            <svg
              className="hover:cursor-pointer"
              onClick={() => router.push("/")}
              xmlns="http://www.w3.org/2000/svg"
              height="1.5em"
              viewBox="0 0 576 512"
            >
              <path
                d="M575.8 255.5c0 18-15 32.1-32 32.1h-32l.7 160.2c.2 35.5-28.5 
                         64.3-64 64.3H128.1c-35.3 0-64-28.7-64-64V287.6H32c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4
                          8c7-7 15-8 22-8s15 2 21 7L564.8 231.5c8 7 12 15 11 24zM352 224a64 64 0 1 0 -128 0 64 64 0
                           1 0 128 0zm-96 96c-44.2 0-80 35.8-80 80c0 8.8 7.2 16 16 16H384c8.8 0 16-7.2 16-16c0-44.2-35
                           .8-80-80-80H256z"
              />
            </svg>
          </div>
          <div>
            <svg
              className="hover:cursor-pointer"
              onClick={() => router.refresh()}
              xmlns="http://www.w3.org/2000/svg"
              height="1.5em"
              viewBox="0 0 512 512"
            >
              <path d="M463.5 224H472c13.3 0 24-10.7 24-24V72c0-9.7-5.8-18.5-14.8-22.2s-19.3-1.7-26.2 5.2L413.4 96.6c-87.6-86.5-228.7-86.2-315.8 1c-87.5 87.5-87.5 229.3 0 316.8s229.3 87.5 316.8 0c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0c-62.5 62.5-163.8 62.5-226.3 0s-62.5-163.8 0-226.3c62.2-62.2 162.7-62.5 225.3-1L327 183c-6.9 6.9-8.9 17.2-5.2 26.2s12.5 14.8 22.2 14.8H463.5z" />
            </svg>
          </div>
          <div>
            <svg
              className="hover:cursor-pointer"
              onClick={() =>
                Number(chapterid) > min
                  ? router.push(
                      `/truyen/${title}/${
                        props.params.chapter
                      }?title=${title}&bookId=${bookId}&chapterId=${
                        Number(chapterid) - 1
                      }`
                    )
                  : null
              }
              xmlns="http://www.w3.org/2000/svg"
              height="1.5em"
              viewBox="0 0 512 512"
            >
              <path d="M459.5 440.6c9.5 7.9 22.8 9.7 34.1 4.4s18.4-16.6 18.4-29V96c0-12.4-7.2-23.7-18.4-29s-24.5-3.6-34.1 4.4L288 214.3V256v41.7L459.5 440.6zM256 352V256 128 96c0-12.4-7.2-23.7-18.4-29s-24.5-3.6-34.1 4.4l-192 160C4.2 237.5 0 246.5 0 256s4.2 18.5 11.5 24.6l192 160c9.5 7.9 22.8 9.7 34.1 4.4s18.4-16.6 18.4-29V352z" />
            </svg>
          </div>
          <div>
            <select
              name=""
              id=""
              className="select"
              onChange={handleSelectChange}
              value={selectedOption}
            >
              {elements}
            </select>
          </div>
          <div>
            <svg
              className="hover:cursor-pointer"
              onClick={() =>
                Number(chapterid) < max
                  ? router.push(
                      `/truyen/${title}/${
                        props.params.chapter
                      }?title=${title}&bookId=${bookId}&chapterId=${
                        Number(chapterid) + 1
                      }`
                    )
                  : null
              }
              xmlns="http://www.w3.org/2000/svg"
              height="1.5em"
              viewBox="0 0 512 512"
            >
              <path d="M52.5 440.6c-9.5 7.9-22.8 9.7-34.1 4.4S0 428.4 0 416V96C0 83.6 7.2 72.3 18.4 67s24.5-3.6 34.1 4.4L224 214.3V256v41.7L52.5 440.6zM256 352V256 128 96c0-12.4 7.2-23.7 18.4-29s24.5-3.6 34.1 4.4l192 160c7.3 6.1 11.5 15.1 11.5 24.6s-4.2 18.5-11.5 24.6l-192 160c-9.5 7.9-22.8 9.7-34.1 4.4s-18.4-16.6-18.4-29V352z" />
            </svg>
          </div>
          <div>
            <button className="hover:cursor-pointer hover:bg-green-400">
              Theo dõi
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
