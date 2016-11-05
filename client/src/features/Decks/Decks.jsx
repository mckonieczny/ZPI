import React from 'react';
import {Paper, TextField, RaisedButton, SelectField,
  MenuItem, GridList, GridTile, Subheader, IconButton, Divider} from 'material-ui';
import StarBorder from 'material-ui/svg-icons/toggle/star-border';
import DeckCard from './DeckCard';
import {Row,Col} from 'react-bootstrap';
import {indigo500} from 'material-ui/styles/colors';
import {browserHistory} from 'react-router';
import _ from 'lodash';
import {connect} from 'react-redux';

class Decks extends React.Component {

  constructor() {
    super();
    this.renderMyDecks = this.renderMyDecks.bind(this);
    this.buttonClicked = this.buttonClicked.bind(this);
  }

  buttonClicked(deckId) {
    browserHistory.push(`/deck/${deckId}`);
  }

   renderMyDecks() {
     return this.props.myDecks.map((deck) => (
       <Col key={deck._id} md={4}>
         <DeckCard key={deck._id} name={deck.name} creatorName={'Not Logged'} actionLabel={'Edit'}
           description={deck.description} onAction={_.partial(this.buttonClicked, deck._id)} />
       </Col>
     ));
   }

   renderOtherDecks() {
     return this.props.otherDecks.map((deck) => (
       <Col md={4}>
         <DeckCard key={deck._id} name={deck.name} creatorName={'Not Logged'} actionLabel={'View'}
           description={deck.description} onAction={_.partial(this.buttonClicked, deck._id)} />
       </Col>
     ));
   }

  render() {
    const headerStyle = {
      marginLeft: '50px',
      fontSize: '30px',
      marginTop: '10px',
      marginBottom: '50px',
      color: indigo500,
      // fontFamily: '"Dancing Script", Georgia, Times, serif',
      fontFamily: '"Roboto", sans-serif'

    };
    return (
      <div>
        <Row>
          <h1 style={headerStyle}>
            My decks
          </h1>
        </Row>
        <Row>
          {this.renderMyDecks()}
        </Row>
        <Row style={{marginTop: '50px'}}>
          <Divider />
        </Row>
        <Row>
          <Col md={7}>
            <h1 style={headerStyle}>
              See also bundles created by others
            </h1>
          </Col>

          <Col md={3}>
            <TextField
              hintText="Search"
            />
          </Col>
          
        </Row>
        <Row>
          {this.renderOtherDecks()}
        </Row>
      </div>

    );
  }
}

Decks.defaultProps = {
    myDecks: [],
    otherDecks: [],
};

const mapStateToProps = state => ({
    decks: state
});

export default connect(mapStateToProps)(Decks);
