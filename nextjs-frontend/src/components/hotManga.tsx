"use client";
import "../styles/hotmangas.scss";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination, Scrollbar, A11y } from "swiper/modules";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import "swiper/css/scrollbar";
import { useEffect, useState } from "react";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
export default function HotManga() {
  const router = useRouter();
  const [dataManga, setDataManga] = useState<any>();
  useEffect(() => {
    const getManga = async () => {
      const res = await axios.get(`http://localhost:8080/api/book/all?page=1`);
      const listManga = await res.data;
      setDataManga(listManga);
    };
    getManga();
  }, []);
  dataManga && console.log(dataManga);
  return (
    <>
      <div className="hotmanga_container">
        <h1 className="px-4 text-xl">Truyện đề cử:</h1>
        <div className="hotmanga_item px-2 gap-4">
          <Swiper
            modules={[Navigation, Pagination, Scrollbar, A11y]}
            spaceBetween={50}
            slidesPerView={5}
            navigation
            pagination={{ clickable: true }}
            // scrollbar={{ draggable: true }}
          >
            {dataManga &&
              dataManga.map((item: any, index: number) => {
                let chapter = JSON.stringify(item.chapters);
                let arrChapter = JSON.parse(chapter);
                let len = arrChapter.length;
                while (index < 7){
                  return (
                    <SwiperSlide key={index}>
                      <div
                        
                        style={{
                          // backgroundImage: `url(http://localhost:8080/api/book/cover-image?filename=${item.coverImage})`,
                          width: "230px",
                          height: "239px",
                          position: "relative",
                        }}
                        className="hotmanga_image overflow-auto hover:cursor-pointer"
                        onClick={() => router.push(`/truyen/${item.id}?chapterCount=${len}&filename=${item.coverImage}`)}
                      >
                        <img src={`http://localhost:8080/api/book/cover-image?filename=${item.coverImage}`} alt="img"
                        style={{
                          width: '100%',
                          height: '100%',
                        }} />
                        <div className="hotmanga_name">
                          <Link href={`/truyen/${item.id}?chapterCount=${len}&filename=${item.coverImage}`}>{item.title}</Link>
                        </div>
                      </div>
                    </SwiperSlide>
                  );
                }
                
              })}
            {/* <SwiperSlide>
              <div
                style={{
                  backgroundImage: 'url("/images/manga/kageno_jitsu.jpg")',
                  width: "230px",
                  height: "245px",
                  position: "relative",
                }}
                className="hotmanga_image"
              >
                <div className="hotmanga_name">
                  <a href="#">Kage no Jitsuryokusha ni Naritakute</a>
                </div>
              </div>
            </SwiperSlide> */}
            {/* <SwiperSlide>
              <div
                style={{
                  backgroundImage: 'url("/images/manga/ex2.jpg")',
                  width: "230px",
                  height: "245px",
                  position: "relative",
                }}
                className="hotmanga_image"
              >
                <div className="hotmanga_name">
                  <a href="#">Gachiakuta</a>
                </div>
              </div>
            </SwiperSlide> */}
            {/* <SwiperSlide>
              <div
                style={{
                  backgroundImage: 'url("/images/manga/ex3.jpg")',
                  width: "230px",
                  height: "245px",
                  position: "relative",
                }}
                className="hotmanga_image"
              >
                <div className="hotmanga_name">
                  <a href="#">Kimi wa meido sama</a>
                </div>
              </div>
            </SwiperSlide> */}
            {/* <SwiperSlide>
              <div
                style={{
                  backgroundImage: 'url("/images/manga/ex4.jpg")',
                  width: "230px",
                  height: "245px",
                  position: "relative",
                }}
                className="hotmanga_image"
              >
                <div className="hotmanga_name">
                  <a href="#">Kajiki no ryourinin</a>
                </div>
              </div>
            </SwiperSlide> */}
            {/* <SwiperSlide>
              <div
                style={{
                  backgroundImage: 'url("/images/manga/ex5.jpg")',
                  width: "230px",
                  height: "245px",
                  position: "relative",
                }}
                className="hotmanga_image"
              >
                <div className="hotmanga_name">
                  <a href="#">Shishidou san ni shikararetai</a>
                </div>
              </div>
            </SwiperSlide> */}
            {/* <SwiperSlide>
              <div
                style={{
                  backgroundImage: 'url("/images/manga/ex6.jpg")',
                  width: "230px",
                  height: "245px",
                  position: "relative",
                }}
                className="hotmanga_image"
              >
                <div className="hotmanga_name">
                  <a href="#">Shishidou san ni shikararetai</a>
                </div>
              </div>
            </SwiperSlide> */}
            {/* <SwiperSlide>
              <div
                style={{
                  backgroundImage: 'url("/images/manga/ex7.jpg")',
                  width: "230px",
                  height: "245px",
                  position: "relative",
                }}
                className="hotmanga_image"
              >
                <div className="hotmanga_name">
                  <a href="#">Shishidou san ni shikararetai</a>
                </div>
              </div>
            </SwiperSlide> */}
          </Swiper>
        </div>
      </div>
    </>
  );
}
