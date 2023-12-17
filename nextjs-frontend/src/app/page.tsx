'use client'
import HotManga from "@/components/hotManga";
import ListManga from "@/components/listManga";
import FollowManga from "@/components/followManga";
import "../styles/styless.scss"
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
export default function Home() {
  const [page, setPage] = useState<number>(1);
  const router = useRouter();
  useEffect(() => {
    router.refresh();
  },[]);
  return (
    <main className="flex min-h-screen flex-col items-center justify-between py-1">
      <HotManga/>
        <div className="container">
            <div className="container_item">
              <div className="list_manga"><ListManga props={page}/></div>
                
                <div className="button_changeChap">
                  <button className="btn" onClick={() => setPage(page - 1)}>Trang trước</button>
                  <button className="btn" onClick={() => setPage(page + 1)}>Trang sau</button>
                </div>
            </div>
            <div className="follow_container">
                <FollowManga/>
            </div>
        </div>
    </main>
  )
}
