import "./App.css";
import PersonView from "./components/PersonView";
import Typography from "@mui/material/Typography";
import Link from "@mui/material/Link";
import { ThemeProvider, createTheme } from '@material-ui/core/styles';

const theme = createTheme();

function App() {

  return <ThemeProvider theme={theme}>
    <div className="App">
      <header className="App-header">
        <PersonView />
        <Typography>
          <Link href="/swagger-ui.html">Swagger UI</Link>&nbsp;
          <Link href="https://github.com/datengaertnerei/test-data-service">GitHub Repo</Link>
        </Typography>
      </header>
    </div>
    </ThemeProvider>;

}

export default App;
