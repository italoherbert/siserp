import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

import pt from "date-fns/locale/pt";
import { registerLocale } from 'react-datepicker';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-datepicker/dist/react-datepicker.css';
import 'react-tabs/style/react-tabs.css';

import './css/estilo.css';
import './font/fontes.css';
import './font/awesome-all.css';
import './font/bootstrap-icons.css';

registerLocale('pt', pt);

ReactDOM.render(
  <React.Fragment>
    <App />
  </React.Fragment>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
