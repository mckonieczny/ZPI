import React from 'react';
import {AddCard, CardsTable} from '../Cards';
import {Row, Col} from 'react-bootstrap';
import {indigo500} from 'material-ui/styles/colors';
import DeleteIcon from 'material-ui/svg-icons/action/delete';
import Synchro from 'material-ui/svg-icons/action/cached';
import FullStar from 'material-ui/svg-icons/toggle/star';
import EmptyStar from 'material-ui/svg-icons/toggle/star-border';
import {FlatButton} from 'material-ui';
import Loader from '../../components/Loader';
import {browserHistory} from 'react-router';


const headerStyle = {
    marginLeft: '50px',
    marginTop: '10px',
    color: indigo500,
    fontFamily: '"Roboto", sans-serif'
};

const deckNameStyle = {
    fontFamily: '"Roboto", sans-serif',
    color: indigo500,
    textAlign: 'center',
};

const buttonStyle = {
    margin: 12,
};


export default class DeckView extends React.Component {

    // constructor(props) {
    //     super(props);
        
    //     this.props.onRemoveFromFavorites.bind(null, props.deck._id);
    //     this.props.onAddToFavorites.bind(null, props.deck._id);
    // }

    renderCardsTable = () => {
        if (this.props.isFetching) {
            return <Loader />;
        }
        return (
            <CardsTable
                deckId={this.props.deck._id}
                cards={this.props.deck.cards}
                eraseable={this.props.isOwner}
                isFetching={this.props.isFetching}
            />
        );
    }

    getFavoriteButtonLabelAndIcon = () => this.props.isFavorite? 
        {label: 'Remove from favorites', icon: <FullStar />, action: this.removeFromFavorites} : 
        {label: 'Add to favorites', icon: <EmptyStar />, action: this.addToFavorites};

    renderFavoriteButton = () => {
        const {label, icon, action} = this.getFavoriteButtonLabelAndIcon();
        return (
              <FlatButton
                    label={label}
                    primary={true}
                    style={buttonStyle}
                    icon={icon}
                    onClick={action}
                />
        )
    }

    deleteDeck = () => {
        browserHistory.push('/decks');
        this.props.onDelete(this.props.deck._id);
    }

    removeFromFavorites = () => {
        this.props.onRemoveFromFavorites(this.props.deck._id);
    }

    addToFavorites = () => {
        this.props.onAddToFavorites(this.props.deck._id);
    }

    

    render () {
        return (
            <div>
                <Row style={deckNameStyle}>
                <h1 >
                    {this.props.deck.name}
                </h1>
                <FlatButton
                    label="save"
                    primary={true}
                    style={buttonStyle}
                    icon={<Synchro />}
                    onClick={this.props.onSynchronize}
                />

                {this.renderFavoriteButton()}

                {this.props.isOwner?
                    <FlatButton
                    label="delete deck"
                    onClick={this.deleteDeck}
                    secondary={true}
                    style={buttonStyle}
                    icon={<DeleteIcon />}
                    />
                : null}
                
                </Row>
                {this.props.isOwner ?
                <div>
                    <Row>
                    <h2 style={headerStyle}>
                        Add new card
                    </h2>
                    </Row>
                    <Row style={{marginBottom: '60px'}}>
                    <Col mdOffset={3} md={5}>
                        <AddCard deckId={this.props.deck._id} />
                    </Col>
                    </Row>
                </div>
                :
                null
                }
                <Row>
                    <h2 style={headerStyle}>
                    Cards to learn
                    </h2>
                </Row>
                <Row style={{marginTop: '20px'}}>
                    {this.renderCardsTable()}
                </Row>

            </div>
    );
  }
}
