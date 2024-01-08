type ChildProps = {
    children: React.ReactNode
    // Phải tên là children thì mới pass 1 node vào được
}

export const Child = (props : ChildProps) => {
    return (
        <div className="child">
            {props.children}
        </div>
    )
}