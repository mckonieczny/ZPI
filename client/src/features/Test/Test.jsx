import React from 'react';

class Test extends React.Component {
    render () {
        const {value} = this.props;
        return (
            <div>
                <button onClick={this.props.action}>
                    Click
                </button>
                <div>
                    <h1>
                        {value}
                    </h1>
                </div>
            </div>
        );
    }
}

export default Test;
