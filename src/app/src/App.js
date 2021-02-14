import "./App.css";
import PersonView from "./components/PersonView";
import Button from "@material-ui/core/Button";
import Link from "@material-ui/core/Link";

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <PersonView />
        <Link href="/swagger-ui.html">Swagger UI</Link>
        <Link href="https://github.com/datengaertnerei/test-data-service">GitHub Repo</Link>
      </header>
    </div>
  );
}

export default App;
