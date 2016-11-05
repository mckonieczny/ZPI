import React from 'react';
import {connect} from 'react-redux';
import {Table, TableHeader, TableRow, TableHeaderColumn, TableBody, TableRowColumn} from 'material-ui';
import CardTableRow from './CardTableRow';
import {deleteCard} from './duck';

class CardsTable extends React.Component {

   renderCards () {
       return this.props.cards.map((card) => (
         <CardTableRow key={card._id} cardId={card._id}
           deckId={this.props.deckId} word={card.word}
           translation={card.translation} eraseable={this.props.eraseable}
           onDelete={this.props.onDelete} />
       ));
     }

  render() {
    return (
      <Table style={{backgroundColor: '#F5F5F5'}}>
        <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
          <TableRow>
            <TableHeaderColumn>Word</TableHeaderColumn>
            <TableHeaderColumn>Translation</TableHeaderColumn>
              <TableHeaderColumn> </TableHeaderColumn>

          </TableRow>
        </TableHeader>
        <TableBody displayRowCheckbox={false} showRowHover={false}>
          {this.renderCards()}
        </TableBody>

      </Table>
    );
  }
}

const mapStateToProps = (state, ownProps) => ({
    cards: state.cards.filter(card => card.deckId === ownProps.deckId),
});

const mapDispatchToProps = dispatch => ({
  onDelete: (cardId) => dispatch(deleteCard(cardId))
});


export default connect(mapStateToProps, mapDispatchToProps)(CardsTable);
