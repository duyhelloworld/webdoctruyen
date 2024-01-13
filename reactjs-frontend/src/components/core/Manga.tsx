import { Rating } from "./Rating"

type MangaProps = {
    id: number,
    title: string,
    releaseDate: string,
    uploadDate: string,
    author: string,
    rating: number,
    coverImage: string,
    chapters?: number[],
    categories?: string[],
    description?: string
}

export const Manga = (props: MangaProps) => {
    return (
        <div>
            <div>
                <img src={props.coverImage} alt={props.title} />
            </div>
            <div>
                <h3>{props.title}</h3>
                <p>{props.releaseDate}</p>
                <p>{props.uploadDate}</p>
                <p>{props.author}</p>
                <Rating rating={props.rating} />
                <div>
                    <h3>Chapters</h3>
                    {props.chapters?.map((chapter) => {
                        return (
                            <button>
                                {chapter}
                            </button>
                        )
                    })}
                </div>
                <div>
                    <h3>Categories</h3>
                    {props.categories?.map((category) => {
                        return (
                            <button>
                                {category}
                            </button>
                        )
                    })}
                </div>
                <div>
                    <h3>Description</h3>
                    <p>{props.description}</p>
                </div>
            </div>
        </div>
    )
}