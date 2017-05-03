// load our default (non specific) css
import 'font-awesome/css/font-awesome.css';
import 'bootstrap/dist/css/bootstrap.css';
import 'app.less';

import {Blinky} from './sample/Blinky';

document.body.onload = () => {
    const ghost: Blinky = new Blinky();
    document.body.innerHTML += '<br>value: ' + ghost.getValue();
};
