import { Actions } from "./Actions"

type GreetProps = {
    name: {
        first: string,
        last: string
    }
    messageCount?: number,
    messages: {
        when: string,
        creator: string,
        action: Actions,
        status: 'loading' | 'success' | 'error'
    }[],
    isLogged? : boolean
}

export const Greet = (props : GreetProps) => {
    // If pass, use Props. else, use 0
    const {messageCount = 0} = props;
    return (
        props.isLogged ? 
        (
            <div>
                <h2>Welcome {props.name.first} {props.name.last}! You miss {messageCount} messages</h2>
                <ul>
                    {
                        props.messages.map((message, index) => {
                            return (
                                <li key={index}>
                                    {message.creator} was {message.action} your post at {message.when} 
                                </li>
                            )
                        })
                    }
                </ul>
            </div>
        )
        : (
            <div>
                <h2>Please login to see the content</h2>
            </div>
        )
    )
}