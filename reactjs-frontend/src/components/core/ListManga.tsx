import axios from "axios";
import { useEffect, useState } from "react";
import { Manga } from "./Manga";

export const ListManga = () => {
    const [mangas, setMangas] = useState([]);

    useEffect(() => {
        const fetchManga = async () => {
            try {
                let response = await axios.get(`http://localhost:8080/book/all`);
                // setManga(response.data);
                console.log(response.data);
            } catch (error) {
                console.error("Error fetch ListManga:", error);
            }
        }
        fetchManga();
    }, [])

    return (
        <div>
            {mangas.map((manga: any) => {
                return (
                    <Manga
                        id={manga.id}
                        title={manga.title}
                        releaseDate={manga.releaseDate}
                        uploadDate={manga.uploadDate}
                        author={manga.author}
                        rating={manga.rating}
                        coverImage={manga.coverImage}
                        chapters={manga.chapters}
                        categories={manga.categories}
                        description={manga.description}
                    />
                )
            })}
        </div>
    )
}