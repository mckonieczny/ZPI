import React from 'react';

class Test extends React.Component {
    render () {
        return (
            <div>
                <button onClick={this.props.onButtonClick} />
                <div>
                    {this.props.counter}
                </div>
            </div>
        );
    }
}

export default Test;
