import React from 'react';
import {Component} from 'react';
import {DropdownButton, ButtonGroup, MenuItem, Button, Carousel} from 'react-bootstrap';

import logo from './assets/Logo.png';
import carousel from './assets/carousel.png';

import './lib/bootstrap.css';
import './lib/lumen.css';
import './lib/animate.css';
import './App.css';

class TopBar extends Component {
    
    constructor(props) {
        super(props);
        this.state = {
            dropDownStates: Array(2).fill(false),
            searching: false
        };
        this.handleHover = this.handleHover.bind(this);
        this.handleSearching = this.handleSearching.bind(this);
    }
    
    handleHover(i){
        const dropDownStates = this.state.dropDownStates.slice();
        dropDownStates[i] = !dropDownStates[i];
        this.setState({
            dropDownStates: dropDownStates
        });
    }
    
    handleSearching(e) {
        this.setState({ searching: e.target.value !== '' });
    }
    
    render() {
        const dropDownClass = [0, 1].map((i) => "dropdown" + this.state.dropDownStates[i]? " open" : "");

        return (
            <div>  
                <nav className = "navbar navbar-expand-lg navbar-dark navbar-fixed-top bg-dark">
                    <div class="container-fluid">
                        <div className = "collapse navbar-collapse bs-navbar-collapse">
                            <ul className = "topbar-left">
                                <img className = "logo animated infinite tada" src={logo} alt="Axiom"></img>
                                
                                <ButtonGroup>
                                    <Button bsStyle="danger" className="navbtn" bsSize="large">GitHub</Button>
                                    <DropdownButton bsStyle="danger" bsSize="large" title="primary" key="0" className={"navbtn " + dropDownClass[0]} noCaret onClick={this.handleHover} onMouseLeave={this.handleHover} id = "bg-nested-dropdwn">
                                      <MenuItem eventKey="1" href="https://github.com/Innoviox/Axiom">GitHub</MenuItem>
                                      <MenuItem eventKey="2">Another action</MenuItem>
                                      <MenuItem eventKey="3" active>Active Item</MenuItem>
                                      <MenuItem divider />
                                      <MenuItem eventKey="4">Separated link</MenuItem>
                                    </DropdownButton>
                                </ButtonGroup>
                            </ul>
                            <ul className = "topbar-right">
                                <form class="form-inline my-2 my-lg-0 search-form">
                                    <input class="form-control mr-sm-2" type="text" placeholder="Search the docs..." onInput = {this.handleSearching}></input>
                                    <button class={"btn " + (this.state.searching?"btn-primary":"btn-info")} type="submit" disabled={!this.state.searching}>Search</button>
                                </form>
                            </ul>
                        </div>
                    </div>
                </nav>
            </div>
        )
    }
}

class MyCarousel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    
    render() {
      return (
          
        <div className="container-fluid">
            <Carousel>
                <Carousel.Item>
                    <div class="wrap">
                        <img width={'100%'} height={'100%'} alt="900x500" src={carousel} />
                        <span class = "text_over_image" id="intro">Welcome to Axiom</span>
                    </div>
                    <Carousel.Caption>
                        <h3>First slide label</h3>
                        <p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
                        <div>
                            <ButtonGroup>
                                <button class="btn btn-primary">Read the docs</button>
                                <a href="https://github.com/Innoviox/Axiom" class="btn btn-info">Go to the <img class="github-btn" src="https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png" alt="GitHub"></img></a>
                            </ButtonGroup>
                        </div>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                  <img width={'100%'} height={'100%'} alt="900x500" src={carousel} />
                  <Carousel.Caption>
                    <h3>Second slide label</h3>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                  </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                  <img width={'100%'} height={'100%'} alt="900x500" src={carousel} />
                  <Carousel.Caption>
                    <h3>Third slide label</h3>
                    <p>Praesent commodo cursus magna, vel scelerisque nisl consectetur.</p>
                  </Carousel.Caption>
                </Carousel.Item>
            </Carousel>
        </div>
      );
    }
};

const App = () =>
    <div className="page">
        <TopBar />
        <MyCarousel />
        { /* <Content /> */ }
    </div>;
    
export default App;