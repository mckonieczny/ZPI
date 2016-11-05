import React from 'react';
import {Card, CardHeader, CardTitle, CardMedia, CardText, RaisedButton, CardActions} from 'material-ui';
import {indigo500} from 'material-ui/styles/colors';
import _ from 'lodash';

const cardStyles = {
    margin: '10px',
};


const DeckCard = ({name, description, creatorName, actionLabel, onAction}) => (

    <Card style={cardStyles} zDepth={5} >
      <CardMedia
        overlay={<CardTitle title={name} subtitle={description} />}
      >
        <img src={`images/deckImages/${_.random(1, 4, false)}.jpg`} />
      </CardMedia>
      {creatorName?
        <CardText >
          {`by ${creatorName}`}
        </CardText>
        :
        null}
        <CardActions>
          <RaisedButton primary fullWidth label={actionLabel} onClick={onAction} />
        </CardActions>
      </Card>
);

export default DeckCard;
