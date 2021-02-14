import React from "react";

class PersonPanel extends React.Component {

    render() {
        var options = { year: "numeric", month: "2-digit", day: "2-digit", hour24: true };
        var bdayString = new Intl.DateTimeFormat("de-DE", options).format(new Date(this.props.person.birthDate));
        return (
            <div>
                <p>{this.props.person.givenName} {this.props.person.familyName}</p>
                <p>{this.props.person.address.streetAddress} {this.props.person.address.houseNumber}</p>
                <p>{this.props.person.address.postalCode}  {this.props.person.address.addressLocality}</p>
                <p>{bdayString} - ID: {this.props.person.taxId}</p>
            </div>
        );
    }
}

export default PersonPanel;