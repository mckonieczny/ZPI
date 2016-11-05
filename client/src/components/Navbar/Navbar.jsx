import React from 'react';
import AppBar from 'material-ui/AppBar';
import MenuItem from 'material-ui/MenuItem';
import IconButton from 'material-ui/IconButton';
import Snackbar from 'material-ui/Snackbar';
import MenuIcon from 'material-ui/svg-icons/navigation/menu';
import FlatButton from 'material-ui/FlatButton';
import Drawer from 'material-ui/Drawer';
import Avatar from 'material-ui/Avatar';
import Divider from 'material-ui/Divider';
import AddBundleIcon from 'material-ui/svg-icons/action/note-add';
import ViewBundlesIcon from 'material-ui/svg-icons/action/view-module';
import AccountBox from 'material-ui/svg-icons/action/account-box';
import Learn from 'material-ui/svg-icons/social/school';
import Create from 'material-ui/svg-icons/content/create';
import {browserHistory} from 'react-router';


import './styles.css';




export default class Navbar extends React.Component {

    constructor () {
        super();
        this.handleOpenMenu = this.handleOpenMenu.bind(this);
        this.handleRequestClose = this.handleRequestClose.bind(this);
        this.handleSnackbarClose = this.handleSnackbarClose.bind(this);
        this.handleSnackbarOpen = this.handleSnackbarOpen.bind(this);
        this.handleLogout = this.handleLogout.bind(this);
        this.handleBadgeClick = this.handleBadgeClick.bind(this);


        this.state = {
            open: false,
            openReport:false,
            snackbarOpen: false,
        };

    }

    closeNavbar = () => {
        this.setState({
            open: false
        });
    }

    goToHomePage = () => {
        browserHistory.push('/home');
        this.closeNavbar();
    }

    goToAddDeck = () => {
        browserHistory.push('/addDeck');
        this.closeNavbar();
    }

    goToDecks = () => {
        browserHistory.push('/decks');
        this.closeNavbar();
    }

    handleLogout () {
        console.log('handleLogout');
    }

    handleOpenMenu (event) {
        event.preventDefault();
        this.setState({
            open: !this.state.open,
        });
    }

    handleRequestClose () {
        this.setState({
            open: false,
        });
    }

    handleSnackbarClose () {
        this.setState({
            snackbarOpen: false
        });
    }

   handleSnackbarOpen () {
        this.setState({
          snackbarOpen: true
        });
   }


   handleBadgeClick () {
       console.log('handleBadgeClick');
   }

    render () {

      const loggedUser = {
          username: 'not logged',
          emails : [
            {address: 'no email address'}
          ]
      };


        const userNameStyle = {
          textAlign: 'center'
        };

        const buttonStyle = {
        backgroundColor: 'transparent',
        color: '#9C27B0',
        paddingTop: '10px',
      };

      const avatarStyle = {
        width: '80px',
        height: '80px',
        position: 'absolute',
        top: '5px',
        left: '20px',

      };


        const titleStyle = {
          color: '#3F51B5',
          fontFamily: '"Dancing Script", Georgia, Times, serif',
          fontSize: '40px',
          textAlign: 'center'
          
        }


        const draweTitleStyle = {
          color: '#3F51B5',
          fontFamily: '"Dancing Script", Georgia, Times, serif',
          fontSize: '25px',
          textAlign: 'center'
          
        }



        return (
          <div className="app-bar">
            <AppBar
              title={this.props.title}
              titleStyle={titleStyle}
              className="app-bar"
              iconElementLeft={<IconButton onClick={this.handleOpenMenu}><MenuIcon /></IconButton>}
              iconElementRight={  <FlatButton label="Log out" style={buttonStyle} onClick={this.handleLogout}/> }
              >

            </AppBar >
            
              <Drawer
                docked={false}
                width={300}
                open={this.state.open}
                onRequestChange={(open) => this.setState({open})}
              >
                <MenuItem style={{textAlign: 'center', marginTop: '20px'}} disabled={true}><span style={draweTitleStyle}>Flash Learn</span></MenuItem>
                <MenuItem style={{textAlign: 'center', marginTop: '20px'}} disabled={true}> <Avatar style={avatarStyle} size={200} src={'/images/defaultAvatar.jpg'} /> </MenuItem>
                <MenuItem style={userNameStyle} disabled={true}>{loggedUser.username}</MenuItem>
                <MenuItem style={userNameStyle} disabled={true}>{loggedUser.emails[0].address}</MenuItem>
                <Divider style={{marginBottom: '50px', marginTop:'10px'}}/>
                <MenuItem onClick={this.goToHomePage} primaryText="My profile" leftIcon={<AccountBox />} />
                <MenuItem onClick={this.goToAddDeck} primaryText="Add deck" leftIcon={<AddBundleIcon />} />
                <MenuItem onClick={this.goToDecks} primaryText="Discover decks" leftIcon={<ViewBundlesIcon />} />
                <MenuItem onClick={this.handleClickMenuItem} primaryText="Compose path" leftIcon={<Create />} href="/addPath"/>
                <MenuItem onClick={this.handleClickMenuItem} primaryText="Explore paths" leftIcon={<Learn />} href="/paths"/>


              </Drawer>



            <Snackbar
            open={this.state.snackbarOpen}
            message="Ups! Funkcjonalność jeszcze nie dostępna."
            autoHideDuration={3000}
            onRequestClose={this.handleSnackbarClose}
          />
        </div>
        );
      }
}

Navbar.defaultProps = {
    title: 'Flash Cards',
};
