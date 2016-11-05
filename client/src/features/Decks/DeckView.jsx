import React from 'react';
import {AddCard, CardsTable} from '../Cards';
import {Row, Col} from 'react-bootstrap';
import {indigo500} from 'material-ui/styles/colors';
import DeleteIcon from 'material-ui/svg-icons/action/delete';
import Synchro from 'material-ui/svg-icons/action/cached';
import {FlatButton} from 'material-ui';
import {browserHistory} from 'react-router';

export default class DeckView extends React.Component {

    constructor () {
        super();
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete (e) {
        console.log('handle delete deck');
        browserHistory.push('/decks');
        this.props.onDelete(this.props.deck._id);
    }


    render () {
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

        return (
            <div>
                <Row style={deckNameStyle}>
                <h1 >
                    {this.props.deck.name}
                </h1>
                <FlatButton
                    label="synchronize"
                    primary={true}
                    style={buttonStyle}
                    icon={<Synchro />}
                    />
                {this.props.isOwner?
                    <FlatButton
                    label="delete deck"
                    onClick={this.handleDelete}
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
                    <CardsTable
                        deckId={this.props.deck._id}
                        cards={this.props.deck.cards}
                        eraseable={this.props.isOwner}
                    />
                </Row>

            </div>
    );
  }
}
