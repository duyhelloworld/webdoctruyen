"use client";
import "../../styles/truyens.scss";
import { useRouter } from "next/navigation";

export default function Chapter(props: any) {
  const router = useRouter();
  const chapter: number = 100;
  const elements: JSX.Element[] = [];
  for (let i: number = 0; i < props.props; i++) {
    elements.push(
      <div className="chapter_item">
        <p onClick={() => router.push("/")}>Chương {i + 1}</p>
        <p>2/12/2023</p>
      </div>
    );
  }
  return (
    <>
      <div className={`chapter_container`}>
        <div className="font-bold text-xl flex items-center">
          <svg
            className="mr-2"
            xmlns="http://www.w3.org/2000/svg"
            height="1em"
            viewBox="0 0 448 512"
          >
            <path d="M0 96C0 78.3 14.3 64 32 64H416c17.7 0 32 14.3 32 32s-14.3 32-32 32H32C14.3 128 0 113.7 0 96zM0 256c0-17.7 14.3-32 32-32H416c17.7 0 32 14.3 32 32s-14.3 32-32 32H32c-17.7 0-32-14.3-32-32zM448 416c0 17.7-14.3 32-32 32H32c-17.7 0-32-14.3-32-32s14.3-32 32-32H416c17.7 0 32 14.3 32 32z" />
          </svg>
          Danh sách chương
        </div>
        {elements}
      </div>
    </>
  );
}
