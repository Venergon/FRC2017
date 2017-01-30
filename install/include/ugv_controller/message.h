/* 
 * File:   udp_message.h
 * Author: will
 *
 * Created on 8 April 2015, 2:50 PM
 */

// THIS FILE NEEDS GNU99, LITTLE ENDIAN

#ifndef UDP_MESSAGE_H
#define	UDP_MESSAGE_H

#include <stdint.h>

#define CONTROLLER_PORT 744

// At the beginning of all packets
struct pkt_header {
	char type;
	uint8_t crc; // NOT USED (UDP checksum instead) crc of all bytes in the message except this one
} __attribute__((packed));

struct timestamp {
    uint32_t tv_sec;
    uint32_t tv_nsec;
};

// ----[Packets received by controller]-----------------------------------------
// A - start stop stream of status packets to the IP that sent this at the
// requested frequency
#define PKT_TYPE_STREAM 'A'
struct pkt_cmd_stream {
	struct pkt_header header;
	uint8_t frequency; // Hz, approx. 0 = stopped
} __attribute__((packed));

// B - set wheel velocity setpoints
#define PKT_TYPE_WHEEL 'B'
struct pkt_cmd_wheel {
	struct pkt_header header;
    // Ticks per SECOND
	int16_t left;
	int16_t right;
} __attribute__((packed));

// C - set PID gains
#define PKT_TYPE_PID 'C'
struct pkt_cmd_pid {
	struct pkt_header header;
	int16_t l_p;
	int16_t l_i;
	int16_t l_d;
	int16_t r_p;
	int16_t r_i;
	int16_t r_d;
} __attribute__((packed));

// D - set LEDs
#define PKT_TYPE_DOUT 'D'
struct pkt_cmd_dout {
    struct pkt_header header;
    unsigned d1:1;
    unsigned d2:1;
    unsigned d3:1;
    unsigned d4:1;
} __attribute__((packed));

// E - run panning motor (speed == 0 for stop)
#define PKT_TYPE_PANM 'E'
struct pkt_cmd_panm {
    struct pkt_header header;
    int16_t speed;
    int32_t left_limit;
    int32_t right_limit;
} __attribute__((packed));

// F - set time
#define PKT_TYPE_TIME 'F'
struct pkt_cmd_time {
    struct pkt_header header;
    struct timestamp time;
} __attribute__((packed));

// G - ping
#define PKT_TYPE_PING 'G'
struct pkt_cmd_ping {
	struct pkt_header header;
} __attribute__((packed));

// H - set pan encoder position
#define PKT_TYPE_PANENC 'H'
struct pkt_cmd_panenc {
  struct pkt_header header;
  int32_t position;
} __attribute__((packed));

// ----[Packets sent by controller]---------------------------------------------
// Z - status streamed back by A command
#define PKT_TYPE_STAT 'Z'
struct pkt_stat_stream {
    struct pkt_header header;
    struct timestamp timestamp;

    // Wheel displacement in ticks
    int32_t l_disp;
    int32_t r_disp;
    // Wheel velocities in ticks per SECOND
    int16_t l_vel;
    int16_t r_vel;

    uint16_t vbat; // in mv
    // Position of panning laser in ticks, sign indicates direction of travel
    int32_t pan_enc;

    uint16_t din; // bit 0 = estop, bit 1 = limit
} __attribute__((packed, aligned(4)));

// Y - response to ping
#define PKT_TYPE_PINGRESP 'Y'
struct pkt_stat_pingresp {
	struct pkt_header header;
} __attribute__((packed));

#endif	/* UDP_MESSAGE_H */
