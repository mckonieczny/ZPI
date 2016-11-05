import React from 'react';
import DeleteIcon from 'material-ui/svg-icons/action/delete';
import IconButton from 'material-ui/IconButton';
import {TableRow, TableRowColumn} from 'material-ui';
import {RaisedButton, FlatButton} from 'material-ui';


export default class CardTableRow extends React.Component {
    constructor () {
        super();
        this.onHover = this.onHover.bind(this);
        this.onHoverExit = this.onHoverExit.bind(this);
        this.handleDelete = this.handleDelete.bind(this);

        this.state = {
            show: false
        };
    }

  onHover(e) {
    if(!this.props.eraseable) {
      return;
    }
    this.setState({
      show: true
    })

  }

  onHoverExit (e) {
    e.preventDefault();
    this.setState({
      show: false
    })
  }

  handleDelete (e) {
    this.props.onDelete(this.props.cardId);
  }



  render() {

    const rowStyle = {
      paddingTop: '20px',
      paddingBottom: '20px',
      fontSize: '15px',
      textAlign: 'center'
    }

    const buttonStyle = {
      margin: 12,
    };
    return(
      <TableRow hoverable={true} displayBorder={false} onCellHover={this.onHover} onCellHoverExit={this.onHoverExit} >
        <TableRowColumn style={rowStyle}>{this.props.word}</TableRowColumn>
        <TableRowColumn style={rowStyle}>{this.props.translation}</TableRowColumn>
        <TableRowColumn >
          {this.state.show ?
            <div>
              <FlatButton
                label="delete"
                onClick={this.handleDelete}
                primary={true}
                style={buttonStyle}
                icon={<DeleteIcon />}
                />
            </div>
            :
            null
          }
        </TableRowColumn>
      </TableRow>
    );
  }
}
