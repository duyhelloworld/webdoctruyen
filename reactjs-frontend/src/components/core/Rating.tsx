import { useEffect, useState } from "react";

type RatingProps = {
    rating: number
}

export const Rating = (props: RatingProps) => {
    const [rating, setRating ] = useState<number[]>([]);

    useEffect(() => {
        setRating(Array(5).fill(0)
            .map((_, index) => index < props.rating ? 1 : 0));
    }, [])

    return (
        <div>
            {rating.map((rate) => {
                return (
                    <div>
                        {rate === 1 ? '★' : '☆'}
                    </div>
                )
            })}
        </div> 
    )
}