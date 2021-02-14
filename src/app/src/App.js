import "./App.css";
import PersonView from "./components/PersonView";
import Typography from "@material-ui/core/Typography";
import Link from "@material-ui/core/Link";

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <PersonView />
        <Typography>
          <Link href="/swagger-ui.html">Swagger UI</Link>&nbsp;
          <Link href="https://github.com/datengaertnerei/test-data-service">GitHub Repo</Link>
        </Typography>
      </header>
    </div>
  );
}

export default App;
