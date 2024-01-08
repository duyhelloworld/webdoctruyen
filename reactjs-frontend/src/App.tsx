import './App.css';
import { Actions } from './components/Actions';
import { Child } from './components/Child';
import { Greet } from './components/Greet';
import { LoremText } from './components/LoremText';

function App() {
  const name = {
    first: 'Phạm',
    last: 'Duy'
  }

  const messages = [
    {
      when: '2023-19-10 20:10',
      creator: 'Nam',
      action: Actions.MAKE_EMOTION,
      status: 'loading' as const
    },
    {
      when: '2023-19-11 20:44',
      creator: 'Hang',
      action: Actions.SHARE,
      status: 'loading' as const
    },
    {
      when: '2023-19-11 19:21',
      creator: 'Duy Quang',
      action: Actions.SHARE,
      status: 'loading' as const
    }
  ]


  return (
    <div className="App">
      <Greet name={name} messageCount={10} isLogged={true} messages={messages} />
      <Child>
        <LoremText />
      </Child>
    </div>
  );
}

export default App;
