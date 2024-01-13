type HeaderProps = {
    children: React.ReactNode,
    categories: string[],
    selectedCategory?: number,
}

export const Header = (props: HeaderProps) => {
    return (
        <div>
            <div>
                {props.children}
            </div>
            <div>
                {props.categories.map((category, index) => {
                    return (
                        <button key={index}>
                            {category}
                        </button>
                    )
                })}
            </div>
        </div>
    )
}