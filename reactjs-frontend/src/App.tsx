import './App.css';
import { Footer } from './components/Footer';
import { Header } from './components/Header';
import { ListManga } from './components/core/ListManga';

export default function App() {
  const categories = ["Action", "Adventure", "Comedy", "Drama", "Fantasy", "Horror", "Mystery", "Romance", "Slice of Life", "Sports", "Supernatural", "Tragedy"];
  return (
    <div className="App">
      <Header categories={categories}>
        <ListManga />
      </Header>
      <Footer />
    </div>
  );
}